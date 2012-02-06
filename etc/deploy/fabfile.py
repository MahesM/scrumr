#
#    fabric -Installed using sudo apt-get install fabric
#    
#    To deploy - change to the private key location  
#
#    fab deploy -i /home/chander/.ssh/chennai.ssh
#
#

from __future__ import with_statement
#from fabric.api import local, settings, abort
from fabric.api import * 
from fabric.contrib.console import confirm

#Warning :Enabling env.disable_known_hosts will leave you wide open 
#to man-in-the-middle attacks! Please use with caution.

env.disable_known_hosts=False
env.reject_unknown_hosts = False
# specify multiple hosts for deploying

# Staging`
env.hosts = ['chennai.pramati.com']
#elastic IP
#184.73.50.160


# QA
#env.hosts = ['ec2-50-16-58-173.compute-1.amazonaws.com']

# dev  - verify if this instance is available, developer instances are typically terminated
#env.hosts = ['ec2-50-16-6-43.compute-1.amazonaws.com']

# Donot -NEVER SPECIFY the PASSWORD here
env.user = 'root'

def git_pull():
    """ Pulls the scrumr branch from github. Assumption- private key pair is available 
    on the server from where this command is run.""" 
    
    local('git pull  git@github.com:Imaginea/scrumr.git', capture=False)
  
  # not complete yet, set our tagging rules  
def tag():
    stable='scrumr-v-1.0.0.0'
    last='scrumr-v-1.0.0.0'
    new='scrumr-v-1.0.0.0'
    
    # tag it
    local('git tag %s' % (new) ) 
    local('git checkout %s' % (new))
    # create the archive 
    #pack() 
    local('git diff %s..%s | gzip -9 > ../patch-%s.gz' % (stable, new, new))
    local('git log --no-merges %s..%s > ../ChangeLog-%s' % (new, last, new))
    local('git shortlog --no-merges %s..%s > ../ShortLog' % (new, last))
    local('git diff --stat --summary -M %s..%s > ../diffstat-%s' % (last, new, new))
    #email the change log to the mailing group
    #push the new tag to the remote git repository


def deploy():
    put('./scrumr.war', '/tmp/apache-tomcat-6.0.26/webapps')
